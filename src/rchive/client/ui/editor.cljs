(ns rchive.client.ui.editor
  (:require
   [reagent.core :as r]
   [rchive.client.ui.placard :as ui.p]))

(defonce placard
  (r/atom nil))

(reset! placard {:placard/title "Octopiggy Bank"
                 :placard/artists ["John Lemme"
                                   "Rafal Dittwald"
                                   "Canna Wen"]
                 :placard/year "2026"
                 :placard/materials "PLA, paint, plastic sphere scavenged from H&M's garbage"
                 :placard/description "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."})

(def rc-green "#23a050")

(defn text-input
  [{:keys [key label]}]
  [:label {:tw "block"}
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   [:input {:tw "w-full rounded bg-white text-black px-2 py-1"
            :value (key @placard)
            :on-change (fn [e]
                         (swap! placard assoc key (.. e -target -value)))}]])

(defn textarea-input
  [{:keys [key label rows] :or {rows 4}}]
  [:label {:tw "block"}
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   [:textarea {:tw "w-full rounded bg-white text-black px-2 py-1 resize-y"
               :rows rows
               :value (key @placard)
               :on-change (fn [e]
                            (swap! placard assoc key (.. e -target -value)))}]])

(defn remove-index
  [v i]
  (into (subvec v 0 i)
        (subvec v (inc i))))

(defn array-input
  [{:keys [key label]}]
  [:div
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   (for [[i item] (map-indexed vector (key @placard))]
     ^{:key i}
     [:div {:tw "flex items-center gap-1 mb-1"}
      [:input {:tw "flex-1 rounded bg-white text-black px-2 py-1"
               :value item
               :on-change (fn [e]
                            (swap! placard assoc-in [key i] (.. e -target -value)))}]
      [:button {:type "button"
                :tw "px-2 py-1 bg-white/20 rounded hover:bg-white/30"
                :on-click (fn [_]
                            (swap! placard update key remove-index i))} "−"]])
   [:button {:type "button"
             :tw "text-sm px-2 py-0.5 bg-white/20 rounded hover:bg-white/30"
             :on-click (fn [_]
                         (swap! placard update key conj ""))} "+"]])

(defn form-view
  []
  [:div#form {:tw "print:hidden text-white p-4"
              :style {:background rc-green}}

   [:form {:tw "space-y-3 min-w-120"}
    [text-input {:label "Title"
                 :key :placard/title}]
    [array-input {:label "Artists"
                  :key :placard/artists}]
    [text-input {:label "Year"
                 :key :placard/year}]
    [text-input {:label "Materials"
                 :key :placard/materials}]
    [textarea-input {:label "Description"
                     :key :placard/description}]]

   [:button {:tw "mt-4 w-full px-4 py-2 bg-white rounded font-semibold hover:bg-white/90"
             :style {:color rc-green}
             :on-click (fn [_]
                         (js/window.print))}
    "Print"]])

(defn editor-view
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

