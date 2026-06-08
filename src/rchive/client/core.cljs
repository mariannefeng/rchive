(ns ^:figwheel-hooks
  rchive.client.core
  (:require
   [reagent.core :as r]
   [bloom.omni.reagent :as rdom]
   [rchive.client.ui.placard :as ui.p]))

(defonce placard
  (r/atom nil))

(reset! placard {:placard/title "Octopiggy Bank"
                 :placard/artists ["John Lemme"
                                   "Rafal Dittwald"
                                   "Canna Wen"]
                 :placard/year "2026"
                 :placard/materials "PLA, paint, scavenged plastic sphere"
                 :placard/description "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."})

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
      [:button {:type "button"
                :on-click (fn [_]
                            (swap! placard update :placard/artists remove-index i))} "-"]])
   [:button {:type "button"
             :on-click (fn [_]
                         (swap! placard update :placard/artists conj ""))} "+"]])

(defn form-view
  []
  [:div#form {:tw "print:hidden"}
   [:form

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
    "Print"]])

(defn app-view
  []
  [:div {:tw "flex gap-2 items-start"}
   [:style
    "@import url('https://fonts.googleapis.com/css2?family=Libre+Franklin:ital,wght@0,100..900;1,100..900&display=swap');
     #placard {
       font-family: \"Libre Franklin\", sans-serif;
     }"]
   [:style {:media "print"}
    ".print\\:hidden {display: none}
     .print\\:border-transparent {border-color: transparent}
     .print\\:block {display: block !important}"]

   [:link {:href "/css/twstyles.css" :media "print" :rel "stylesheet"}]

   [form-view]

   [:div {:tw "space-y-4 bg-gray-100"}
    [ui.p/placard-view @placard]
    #_[ui.p/placard-vertical-no-stripe @placard]
    #_[ui.p/placard-horizontal-with-stripe @placard]]])

(defn render []
  (rdom/render [app-view]))

(defn ^:after-load reload
  []
  (render))

(defn ^:export init []
  (render))

