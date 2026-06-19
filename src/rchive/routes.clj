(ns rchive.routes
  (:require
   [tada.events.core :as tada]
   [tada.events.ring :as tada.ring]
   [ring.middleware.oauth2 :as oauth]
   [rchive.db :as db]
   [rchive.rcapi :as rcapi]
   [rchive.git :as git]
   [rchive.config :as config]))

(defonce t (tada/init :malli))

(def events
  [{:id :api/user
    :params {:token [:maybe :any]}
    :return
    (fn [{:keys [token]}]
      (rcapi/me token))}


   ;; PUBLIC
   ;; anyone can view all the placards

   {:id :api/config
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

   ;; RESTRICTED

   ;; for these, just assuming that having a token set is good enough
   ;; (token may have expired, but we're not using it)

   {:id :api/create-placard!
    :params {:token :string}
    :effect (fn [_]
              {:id (db/create!)})
    :return :tada/effect-return}

   {:id :api/update-placard!
    :params {:token :string
             :placard db/Placard}
    :effect (fn [{:keys [placard token]}]
              (db/update! placard)
              (when-let [git-config (config/get :git)]
                (let [{:keys [email first_name last_name]} (rcapi/memo-me token)
                      git-opts (assoc git-config
                                      :author-email email
                                      :author-name (str first_name " " last_name))]
                  (git/add-commit-and-push!
                   git-opts
                   (str "update placard " (:placard/id placard))))))}])

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
             ;; must be assoced after
             ;; or else someone can sneak a token in via other params
             (assoc :token
                    (get-in request [:session :token]))))
        (throw (ex-info "Incorrect TADA params" {}))))))

(defn oauth-middleware
  [handler]
  (oauth/wrap-oauth2 handler {:rc (config/get :oauth)}))

;; after oauth (:session request) contains:
#_{:ring.middleware.oauth2/access-tokens
   {:rc
    {:token "REDACTED"
     :extra-data {:token_type "Bearer"
                  :scope "public"
                  :created_at 1781026058}
     :expires #inst "2026-06-09T19:27:38.392-00:00"
     :refresh-token "REDACTED"}}}

(def routes
  [
   [[:get "/oauth/log-out"]
    (fn [_]
      {:status 302
       :session nil
       :headers {"Location" "/"}})]

   ;; hack to get oauth-middleware integrated with omni
   [[:any "/oauth/*"]
    (fn [_])
    [oauth-middleware]]

   [[:get "/oauth/rc/upgrade"]
    (fn [request]
      {:status 302
       :session {:token (get-in request [:session ::oauth/access-tokens :rc :token])}
       :headers {"Location" "/"}})
    [oauth-middleware]]

   ;; generic tada handler
   [[:post "/api/tada/*"]
    ;; expects body to have {:event-id _ :event-params _}
    (make-tada-handler :body-params)]


   [[:get "/:shortcode"]
    (fn [request]
      (let [shortcode (get-in request [:params :shortcode])]
        (if (re-matches db/shortcode-re shortcode)
          (if-let [placard (db/by-shortcode shortcode)]
            {:status 302
             :headers {"Location" (str "/placard/" (:placard/id placard))}}
            {:status 404
             :headers {"Content-Type" "text/html"}
             :body "Whoops, this placard is unknown. <a href=\"/\">Show All</a>."})
          ;; otherwise, fall through to next request handler
          nil)))]])

