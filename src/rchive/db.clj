(ns rchive.db
  (:require
   [bloom.commons.uuid :as uuid]
   [malli.core :as m]
   [clojure.edn :as edn]
   [bloom.commons.file-db :as fdb]
   [rchive.config :as config])
  (:import
   [java.time LocalDate]))

(def Placard
  [:map
   [:placard/id :uuid]
   [:placard/shortcode :string] ;; TODO max len 4
   [:placard/title :string]
   [:placard/artists [:vector :string]]
   [:placard/year :string] ;; TODO int
   [:placard/materials :string]
   [:placard/description :string]])

(def shortcode-re #"[a-zA-Z0-9]{4}")
(def shortcode-chars "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
(def shortcode-length 4)

(defn shortcode []
  (->> (repeatedly (fn []
                     (rand-nth (vec shortcode-chars))))
       (take shortcode-length)
       (apply str)))

(defn all []
  (fdb/all {:data-path (config/get :data-dir)}))

(defn by-shortcode
  [shortcode]
  (->> (all)
       (filter (fn [p]
                 (= (:placard/shortcode p)
                    shortcode)))
       first))

(defn new-unique-shortcode []
  (let [existing (->> (all)
                      (map :placard/shortcode)
                      set)]
    ;; assuming we will always get one
    ;; if all are used already, it loops forever, oops
    ;; ¯\_(ツ)_/¯
    (->> (repeatedly shortcode)
         (remove existing)
         first)))

#_(new-unique-shortcode)

(defn create! []
 (let [id (uuid/random)
       placard {:placard/id id
                :placard/shortcode (new-unique-shortcode)
                :placard/title "Untitled"
                :placard/artists ["John Smith"]
                :placard/year (str (.getYear (LocalDate/now)))
                :placard/materials "Materials"
                :placard/description "Lorem ipsum..."}]
   (fdb/write-entity! {:data-path (:repo-dir (config/get :git))} placard)
   id))

(defn update!
  [placard]
  (fdb/write-entity! {:data-path (:repo-dir (config/get :git))} placard))

