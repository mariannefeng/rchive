(ns ^:figwheel-hooks
  rchive.client.core
  (:require
   [bloom.omni.reagent :as rdom]
   [bloom.commons.pages :as pages]
   [rchive.client.auth :as auth]
   ;; register pages:
   [rchive.client.pages.editor]
   [rchive.client.pages.gallery]
   [rchive.client.pages.placard]))

(defn app-view
  []
  [:div
   [auth/auth-view]
   [pages/current-page-view]])

(defn render []
  (rdom/render [app-view]))

(defn ^:after-load reload
  []
  (render))

(defn ^:export init []
  (render)
  (pages/initialize! []))


