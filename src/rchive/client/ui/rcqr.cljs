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

(defn rc-frame-view
  [{:keys [width]}]
  (let [h-squares (count rc-frame)
        w-squares (count (first rc-frame))]
    [:svg
     {:style {:width width
              :height (str "calc(" h-squares "/" w-squares " * " width ")")}
      :view-box (str "0 0 " w-squares " " h-squares)}
     (for [[y row] (map-indexed vector rc-frame)]
       ^{:key y}
       [:<>
        (for [[x cell] (map-indexed vector row)]
          (when (= cell 1)
            ^{:key x}
            [:rect
             {:x x
              :y y
              :width 1
              :height 1
              :fill "black"}]))])]))

;; calc is used to allow for any css width, not just px

(defn rc-qr-code
  [{:keys [width text]}]
  [:div {:tw "relative"}
   [rc-frame-view {:width width}]
   [:div {:style {:position "absolute"
                  :left (str "calc( 1/12 * " width ")")
                  :top (str "calc( 1/12 * " width ")")}}
    [qr/qr-code-view {:text text
                      :pad-count 1
                      :size (str "calc( 10/12 * " width ")")}]]])


