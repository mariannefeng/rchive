(ns rchive.omni-config
  (:require
    [rchive.config :as config]
    [rchive.routes :as routes]))

(def omni-config
  (delay
    {:omni/http-port (config/get :http-port)
     :omni/title "RChive"
     :omni/environment (config/get :environment)
     :omni/cljs {:main "rchive.client.core"}
     :omni/css {:tailwind? true}
     :omni/js-scripts []
     :omni/html-head-includes
     [[:link {:rel "stylesheet"
              :href "/css/markdown.css"}]
      [:link {:rel "stylesheet"
              :href "https://fonts.googleapis.com/css2?family=Libre+Franklin:ital,wght@0,100..900;1,100..900&display=swap"}]]
     :omni/auth {:cookie {:name "rchive"
                          :same-site :lax ;; for oauth :(
                          :secret (config/get :auth-cookie-secret)}}
     :omni/api-routes #'routes/routes}))

(def omni-config-hack
  {:omni/environment :prod
   :omni/cljs {:main "rchive.client.core"}
   :omni/css {:tailwind? true}})
