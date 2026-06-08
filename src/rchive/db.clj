(ns rchive.db
  (:require
   [malli.core :as m]
   [clojure.edn :as edn]
   [bloom.commons.file-db :as fdb]
   [rchive.config :as config]))

(def Placard
  [:map
   [:placard/id :uuid]
   [:placard/shortcode :string] ;; TODO max len 4
   [:placard/title :string]
   [:placard/artists [:vector :string]]
   [:placard/year :string] ;; TODO int
   [:placard/materials :string]
   [:placard/description :string]])

(defn all []
  (fdb/all {:data-path (:repo-dir (config/get :git))}))

(defn update!
  [placard]
  (fdb/write-entity! {:data-path (:repo-dir (config/get :git))} placard))

