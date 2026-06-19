(ns rchive.client.auth
  (:require
   [reagent.core :as r]
   [rchive.client.remote :as remote]))

(defonce *user (r/atom nil))

(defn authed?
  []
  (boolean @*user))

(defn check-auth! []
  (-> (remote/tada! [:api/user])
      (.then (fn [user]
               (reset! *user user)))))

(check-auth!)

(defn auth!
  ([]
   (auth! js/window.location.pathname))
  ([redirect-path]
   (set! js/window.location
         (str "/oauth/rc/initiate?return_to=" redirect-path))))

(defn auth-view
  []
  [:div {:tw "absolute top-0 right-0 print:hidden m-2 bg-#23a050 rounded"}
   (if (authed?)
     [:div {:tw "flex items-center"}
      [:img {:tw "w-6 h-6 ml-1"
             :src (:image @*user)}]
      [:a {:tw "px-1 text-white"
           :href "/oauth/log-out"} "×"]]
     [:button {:tw "px-1 text-white"
               :on-click (fn []
                           (auth!))}
      "Log In"])])


