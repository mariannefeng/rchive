(ns rchive.omni-config
  (:require
    [rchive.config :as config]
    [rchive.routes :as routes]))

(def omni-config
  {:omni/http-port (config/get :http-port)
   :omni/title "RChive"
   :omni/environment (config/get :environment)
   :omni/cljs {:main "rchive.client.core"}
   :omni/css {:tailwind? true}
   :omni/js-scripts []
   :omni/auth {:cookie {:name "rchive"
                        :secret (config/get :auth-cookie-secret)}}
   :omni/api-routes #'routes/routes})
