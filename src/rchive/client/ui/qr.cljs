(ns rchive.client.ui.qr
  (:require
   [reagent.core :as r]
   [cljsjs.qrcode-generator]))

(defn qr-code-view
  [{:keys [text size pad-count]}]
  ;; see https://kazuhikoarase.github.io/qrcode-generator/js/demo/
  (r/with-let [qr (doto (js/qrcode 1 "L")
                    (.addData text)
                    (.make))]
    (let [size (or size "100px")
          pad-count (or pad-count 0)
          module-count (.getModuleCount qr)
          tile-size 1
          view-box-size (+ (* module-count tile-size) (* 2 pad-count))]
      [:svg
       {:style {:width size
                :height size}
        :view-box (str "0 0 " view-box-size " " view-box-size)}
       (for [row (range module-count)
             col (range module-count)
             :when (.isDark qr row col)]
         ^{:key (str row "-" col)}
         [:rect
          {:x (+ (* col tile-size) pad-count)
           :y (+ (* row tile-size) pad-count)
           :width tile-size
           :height tile-size
           :fill "black"}])])))
