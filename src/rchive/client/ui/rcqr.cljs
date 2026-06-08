(ns rchive.client.ui.rcqr
  (:require
   [rchive.client.ui.qr :as qr]))

(def rc-frame
  [[1 1 1 1 1 1 1 1 1 1 1 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 0 0 0 0 0 0 0 0 0 0 1]
   [1 1 1 1 1 1 1 1 1 1 1 1]
   [0 0 0 0 1 1 1 1 0 0 0 0]
   [0 1 1 1 1 1 1 1 1 1 1 0]
   [1 1 1 0 1 0 1 0 1 0 1 1]
   [1 1 0 1 0 1 0 1 0 1 1 1]
   [1 1 1 1 1 1 1 1 1 1 1 1]])

(defn rc-qr-code
  [{:keys [width text]}]
  (let [h-squares (count rc-frame)
        w-squares (count (first rc-frame))]
    [:svg
     {:style {:width width
              :height (str "calc(" h-squares "/" w-squares " * " width ")")}
      :view-box (str "0 0 " w-squares " " h-squares)}
     [:g
      (for [[y row] (map-indexed vector rc-frame)]
        ^{:key y}
        [:g
         (for [[x cell] (map-indexed vector row)]
           (when (= cell 1)
             ^{:key x}
             [:rect
              {:x x
               :y y
               :width 1
               :height 1
               :fill "black"}]))])]
     (let [rects (qr/modules {:text text
                              :logical-width 10
                              :pad-count 1})]
       [:g {:transform "translate(1 1)"}
        rects])]))




