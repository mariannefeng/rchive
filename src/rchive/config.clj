(ns rchive.config
  (:refer-clojure :exclude [get])
  (:require
   [bloom.commons.config :as config]
   [rchive.git :as git]))

(def config
  (config/read
   "config.edn"
   [:map
    [:http-port :int]
    [:environment [:enum :prod :dev]]
    [:auth-cookie-secret :string] ;; 16 chars
    [:git git/GitConfig]]))

(defn get [k]
  (clojure.core/get config k))
