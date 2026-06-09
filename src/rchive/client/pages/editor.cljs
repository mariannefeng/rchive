(ns rchive.client.pages.editor
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.remote :as remote]
   [rchive.client.ui.placard :as ui.p]))

(def rc-green "#23a050")

(defn text-input
  [{:keys [*placard key label]}]
  [:label {:tw "block"}
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   [:input {:tw "w-full rounded bg-white text-black px-2 py-1"
            :value (key @*placard)
            :on-change (fn [e]
                         (swap! *placard assoc key (.. e -target -value)))}]])

(defn textarea-input
  [{:keys [*placard key label rows] :or {rows 4}}]
  [:label {:tw "block"}
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   [:textarea {:tw "w-full rounded bg-white text-black px-2 py-1 resize-y"
               :rows rows
               :value (key @*placard)
               :on-change (fn [e]
                            (swap! *placard assoc key (.. e -target -value)))}]])

(defn remove-index
  [v i]
  (into (subvec v 0 i)
        (subvec v (inc i))))

(defn array-input
  [{:keys [*placard key label]}]
  [:div
   [:div {:tw "text-sm font-medium mb-0.5"} label]
   (for [[i item] (map-indexed vector (key @*placard))]
     ^{:key i}
     [:div {:tw "flex items-center gap-1 mb-1"}
      [:input {:tw "flex-1 rounded bg-white text-black px-2 py-1"
               :value item
               :on-change (fn [e]
                            (swap! *placard assoc-in [key i] (.. e -target -value)))}]
      [:button {:type "button"
                :tw "px-2 py-1 bg-white/20 rounded hover:bg-white/30"
                :on-click (fn [_]
                            (swap! *placard update key remove-index i))} "−"]])
   [:button {:type "button"
             :tw "text-sm px-2 py-0.5 bg-white/20 rounded hover:bg-white/30"
             :on-click (fn [_]
                         (swap! *placard update key conj ""))} "+"]])

(defn form-view
  [*placard]
  [:div#form {:tw "print:hidden text-white p-6 h-screen flex flex-col overflow-y-auto"
              :style {:background rc-green}}

   [:form {:tw "space-y-3 min-w-120"}
    [text-input {:label "Title"
                 :*placard *placard
                 :key :placard/title}]
    [array-input {:label "Artists"
                  :*placard *placard
                  :key :placard/artists}]
    [text-input {:label "Year"
                 :*placard *placard
                 :key :placard/year}]
    [text-input {:label "Materials"
                 :*placard *placard
                 :key :placard/materials}]
    [textarea-input {:label "Description"
                     :*placard *placard
                     :key :placard/description}]]

   [:div {:tw "grow"}]

   [:div {:tw "flex gap-4 items-center mt-4"}
    [:a {:tw "px-4 py-2 rounded text-white font-semibold hover:bg-white/20"

         :href (pages/path-for [:page/placard {:id (:placard/id @*placard)}])}
     "Close"]
    [:button {:tw "px-4 py-2 rounded text-white font-semibold hover:bg-white/20"
              :on-click (fn [_]
                          (js/window.print))}
     "Print"]
    [:div {:tw "grow"}]
    [:button {:tw "px-4 py-2 bg-white rounded font-semibold hover:bg-white/90"
              :style {:color rc-green}
              :on-click (fn [_]
                          (remote/tada! [:api/update-placard! {:placard @*placard}]))}
     "Save"]]])

(defn page-view
  [[_ {:keys [id]}]]
  (r/with-let
   [*placard (r/atom nil)
    _ (-> (remote/tada! [:api/placard {:id id}])
          (.then (fn [p]
                   (reset! *placard p))))]

   [:div {:tw "flex items-start w-full h-screen"}
    [:style
     "@import url('https://fonts.googleapis.com/css2?family=Libre+Franklin:ital,wght@0,100..900;1,100..900&display=swap');
     #placard {
               font-family: \"Libre Franklin\", sans-serif;
               }"]
    [:style {:media "print"}
     ".print\\:hidden {display: none !important}
     .print\\:border-transparent {border-color: transparent}
     .print\\:block {display: block !important}"]

    [:link {:href "/css/twstyles.css" :media "print" :rel "stylesheet"}]

    [form-view *placard]

    [:div {:tw "bg-gray-100 flex flex-col items-center justify-center grow h-screen"}
     [ui.p/placard-view {:show-qr? true} @*placard]
     #_[ui.p/placard-vertical-no-stripe {:show-qr? true} @placard]
     #_[ui.p/placard-horizontal-with-stripe {:show-qr? true} @placard]]]))

(pages/register-page!
 {:page/id :page/editor
  :page/view #'page-view
  :page/path "/placard/:id/edit"
  :page/parameters {:id :uuid}})
