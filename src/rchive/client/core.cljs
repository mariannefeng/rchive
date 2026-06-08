(ns ^:figwheel-hooks
  rchive.client.core
  (:require
   [bloom.omni.reagent :as rdom]
   [rchive.client.ui.editor :as editor]))

(defn app-view
  []
  [editor/editor-view])

(defn render []
  (rdom/render [app-view]))

(defn ^:after-load reload
  []
  (render))

(defn ^:export init []
  (render))

