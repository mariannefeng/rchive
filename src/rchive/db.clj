(ns rchive.db
  (:require
   [malli.core :as m]
   [clojure.edn :as edn]))

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
  (->> "placards.edn"
       slurp
       edn/read-string

       cycle
       (take 10)

       (filter (fn [placard]
                 (m/validate Placard placard)))
       ))
