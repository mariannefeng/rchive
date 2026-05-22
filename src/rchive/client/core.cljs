(ns ^:figwheel-hooks
  rchive.client.core
  (:require
   [reagent.core :as r]
   [bloom.omni.reagent :as rdom]))

(defonce placard
  (r/atom {:placard/title "Octopiggy Bank"
           :placard/artists ["Rafal Dittwald" "Canna Wen" "John Lemme"]
           :placard/year "2026"
           :placard/materials "Plastic, 3d print, paint"
           :placard/description "...description TODO"}))


(defn text-input
  [{:keys [key label]}]
  [:label {:tw "block"}
   [:div label]
   [:input {:tw "border"
            :value (key @placard)
            :on-change (fn [e]
                         (swap! placard assoc key (.. e -target -value)))}]])

(defn remove-index
  [v i]
  (into (subvec v 0 i)
        (subvec v (inc i))))

(defn array-input
  [{:keys [key label]}]
  [:label {:tw "block"}
   [:div label]
   (for [[i item] (map-indexed vector (key @placard))]
     ^{:key i}
     [:div
      [:input {:tw "border block"
                    :value item
                    :on-change (fn [e]
                                 (swap! placard assoc-in [key i] (.. e -target -value)))}]
      [:button {
                :type "button"
                :on-click (fn [_]
                            (swap! placard update :placard/artists remove-index i))} "-"]])
   [:button {:type "button"
             :on-click (fn [_]
                         (swap! placard update :placard/artists conj ""))} "+"]])


(defn app-view
  []
  [:div {:tw "flex gap-2"}
   [:style {:media "print"}
    "print:hidden {display: none}"]

   [:link {:href "/css/twstyles.css" :media "print" :rel "stylesheet"}]

   [:div#form
    [:form {:tw "print:hidden"}

     [text-input {:label "Title"
                  :key :placard/title}]
     [array-input {:label "Artists"
                  :key :placard/artists}]
     [text-input {:label "Year"
                  :key :placard/year}]
     [text-input {:label "Materials"
                  :key :placard/materials}]
     [text-input {:label "Description"
                  :key :placard/description}]]

    [:button {:on-click (fn [_]
                          (js/window.print))}
     "Print"]]

   [:div {:tw "p-2 border text-red-500"
          :style {:width "50mm"}}
    [:div (:placard/title @placard)]
    [:div (for [[i item] (map-indexed vector (:placard/artists @placard))]
            ^{:key i}
            [:div item])]
    [:div (:placard/description @placard)]
    [:div (:placard/materials @placard)]
    [:div (:placard/year @placard)]]])

(defn render []
  (rdom/render [app-view]))

(defn ^:after-load reload
  []
  (render))

(defn ^:export init []
  (render))

