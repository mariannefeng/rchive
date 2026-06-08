(ns rchive.routes
  (:require
   [tada.events.core :as tada]
   [tada.events.ring :as tada.ring]
   [rchive.db :as db]))

(defonce t (tada/init :malli))

(def events
  [{:id :api/placards
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
           first))}])

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
