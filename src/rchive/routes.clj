(ns rchive.routes
  (:require
   [tada.events.core :as tada]
   [tada.events.ring :as tada.ring]
   [rchive.db :as db]
   [rchive.git :as git]
   [rchive.config :as config]))

(defonce t (tada/init :malli))

(def events
  [{:id :api/config
    :return
    (fn [_]
      {:config/qr-base-domain
       (config/get :qr-base-domain)})}

   {:id :api/placards
    :return
    (fn [_]
      (db/all))}

   {:id :api/placard
    :params {:id :uuid}
    :return
    (fn [{:keys [id]}]
      ;; hack for now
      (->> (db/all)
           (filter (fn [p]
                     (= id (:placard/id p))))
           first))}

   {:id :api/create-placard!
    :effect (fn [_]
              {:id (db/create!)})
    :return :tada/effect-return}

   {:id :api/update-placard!
    :params {:placard db/Placard}
    :effect (fn [{:keys [placard]}]
              (db/update! placard)
              (git/add-commit-and-push!
               (config/get :git)
               (str "update placard " (:placard/id placard))))}])

(tada/register! t events)

(defn make-tada-handler
  [request->tada-params]
  (fn [request]
    (let [{:tada.event/keys [id params]} (request->tada-params request)]
      (if (and (keyword? id)
               (map? params))
        (tada.ring/ring-dispatch-event!
         t
         id
         (-> params
             (assoc :user-id (get-in request [:session :id]))))
        (throw (ex-info "Incorrect TADA params" {}))))))


(def routes
  [
   ;; generic tada handler
   [[:post "/api/tada/*"]
    ;; expects body to have {:event-id _ :event-params _}
    (make-tada-handler :body-params)
    ]
   ]
  )
