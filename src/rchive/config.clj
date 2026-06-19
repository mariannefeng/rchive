(ns rchive.config
  (:refer-clojure :exclude [get])
  (:require
   [bloom.commons.config :as config]
   [rchive.git :as git]))

(def AbsoluteURL
  :string)

(def RelativeURL
  :string)

(def OauthConfig
  [:map
   [:authorize-uri AbsoluteURL]
    [:access-token-uri AbsoluteURL]
    [:client-id :string]
    [:client-secret :string]
    [:launch-uri RelativeURL]
    [:redirect-uri RelativeURL]
    [:landing-uri RelativeURL]])

(def config
  (delay
    (config/read
     (or (System/getenv "CONFIG_PATH")
         "config.edn")
     [:map
      [:http-port :int]
      [:environment [:enum :prod :dev]]
      [:auth-cookie-secret :string] ;; 16 chars
      [:qr-base-domain :string]
      [:git [:maybe git/GitConfig]]
      [:oauth OauthConfig]])))

(defn get [k]
  (clojure.core/get @config k))
