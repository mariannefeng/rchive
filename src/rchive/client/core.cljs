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
     "Print"]]

   [:div {:tw "space-y-4 bg-gray-100"}
    #_[:div#placard
     {:tw "p-10 border relative m-4 print:border-transparent break-inside-avoid"
      :style {:width "150mm"}}

     [:div {:tw "italic font-light"} (interpose ", " (:placard/artists @placard))]

     [:div {:tw "text-3xl pt-4 pb-1"}
      [:span {:tw "font-bold"}
       (:placard/title @placard) ", "]
      [:span {:tw "font-thin"} (:placard/year @placard)]]

     [:div {:tw "uppercase text-xs tracking-wider font-light"} (:placard/materials @placard)]

     [:div {:tw "pt-4 font-light"} (:placard/description @placard)]

     [:img {:tw "absolute top-10 -mt-2 -mr-2 right-10 w-22 h-22"
            :src "/qr_sample.svg"}]


     [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
     [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]]

    
    [:div#placard
     {:tw "p-10 border relative m-4 pxrint:border-transparent break-inside-avoid bg-white  border-l-1em box-content"
      :style {:width "150mm"}}

     [:div {:tw "italic font-light"} (interpose ", " (:placard/artists @placard))]

     [:div {:tw "text-3xl pt-4 pb-1"}
      [:span {:tw "font-bold"}
       (:placard/title @placard) ", "]
      [:span {:tw "font-thin"} (:placard/year @placard)]]

     [:div {:tw "uppercase text-xs tracking-wider font-light"} (:placard/materials @placard)]

     [:div {:tw "flex gap-7 pt-4"}
      [:div {:tw "font-light"} (:placard/description @placard)]
      [:div {:tw "w-55"}
       [:img {:tw "pt-2"
              :src "/rc-qr.svg"}]]]

     [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
     [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]]




    [:div#placard
     {:tw "p-10 border relative m-4 pxrint:border-transparent break-inside-avoid bg-white border-l-1em box-content"
      :style {:width "120mm"}}

     [:div {:tw "italic font-light"} (interpose ", " (:placard/artists @placard))]

     [:div {:tw "text-3xl pt-4 pb-1"}
      [:span {:tw "font-bold"}
       (:placard/title @placard) ", "]
      [:span {:tw "font-thin"} (:placard/year @placard)]]

     [:div {:tw "uppercase text-xs tracking-wider font-light"} (:placard/materials @placard)]

     [:div {:tw "pt-4 font-light"} (:placard/description @placard)]


     [:div {:tw "flex justify-end"}
      [:img {:tw "-mr-2 mt-20 w-22 h-22"
             :src "/rc-qr.svg"}]]

     [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
     [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
     [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]]


    [:div {:tw "flex xborder m-4 relative bg-white"}
     [:div {:tw "flex justify-end"}
      [:img {:tw "w-71 h-71 p-10 -mr-12"
             :src "/rc-qr.svg"}]]
     [:div#placard
      {:tw " p-10 relative print:border-transparent break-inside-avoid"
       :style {:width "150mm"}}

      [:div {:tw "italic font-light"} (interpose ", " (:placard/artists @placard))]

      [:div {:tw "text-3xl pt-4 pb-1"}
       [:span {:tw "font-bold"}
        (:placard/title @placard) ", "]
       [:span {:tw "font-thin"} (:placard/year @placard)]]

      [:div {:tw "uppercase text-xs tracking-wider font-light"} (:placard/materials @placard)]

      [:div {:tw "pt-4 font-light"} (:placard/description @placard)]


      [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
      [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
      [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
      [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]]

     ]
    
    #_[:div {:tw "flex xborder m-4 relative bg-white"}
     [:div#placard
      {:tw " p-10 relative print:border-transparent break-inside-avoid"
       :style {:width "150mm"}}

      [:div {:tw "italic font-light"} (interpose ", " (:placard/artists @placard))]

      [:div {:tw "text-3xl pt-4 pb-1"}
       [:span {:tw "font-bold"}
        (:placard/title @placard) ", "]
       [:span {:tw "font-thin"} (:placard/year @placard)]]

      [:div {:tw "uppercase text-xs tracking-wider font-light"} (:placard/materials @placard)]

      [:div {:tw "pt-4 font-light"} (:placard/description @placard)]


      [:div {:tw "border-b border-r w-2 h-2 -top-2 -left-2 absolute hidden print:block"}]
      [:div {:tw "border-t border-r w-2 h-2 -bottom-2 -left-2 absolute hidden print:block"}]
      [:div {:tw "border-t border-l w-2 h-2 -bottom-2 -right-2 absolute hidden print:block"}]
      [:div {:tw "border-b border-l w-2 h-2 -top-2 -right-2 absolute hidden print:block"}]]

     [:div {:tw "flex justify-end"}
      [:img {:tw "w-71 h-71 p-10"
             :src "/rc-qr.svg"}]]]


    ]])

(defn render []
  (rdom/render [app-view]))

(defn ^:after-load reload
  []
  (render))

(defn ^:export init []
  (render))

