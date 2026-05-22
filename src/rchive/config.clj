(ns rchive.config
  (:refer-clojure :exclude [get])
  (:require
   [bloom.commons.config :as config]))

(def config
  (config/read
   "config.edn"
   [:any]))

(defn get [k]
  (clojure.core/get config k))
