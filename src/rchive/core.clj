(ns rchive.core
  (:gen-class)
  (:require
    [bloom.omni.core :as omni]
    [rchive.omni-config :refer [omni-config]]))

(defn start! []
  (omni/start! omni-config))

(defn stop! []
  (omni/stop!))

(defn -main []
  (start!))

#_(start!)
