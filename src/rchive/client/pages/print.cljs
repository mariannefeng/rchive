(ns rchive.client.pages.print
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.remote :as remote]
   [rchive.client.ui.placard :as ui.p]))

(defn page-view
  [[_ {:keys [id]}]]
  (r/with-let
    [*placard (remote/tada-atom [:api/placard {:id id}])
     *triggered? (r/atom false)]
    (let [placard @*placard]
      (when (and placard (not @*triggered?))
        (reset! *triggered? true)
        ;; delay slightly, because otherwise, the print css is missing?
        (r/after-render (fn [] (js/setTimeout #(js/window.print) 250))))
      (when placard
        [:div
         [:style {:media "print"}
          ".print\\:hidden {display: none !important}
          .print\\:border-transparent {border-color: transparent}
          .print\\:block {display: block !important}"]

         ;; by default, omni tw styles are screen only
         [:link {:href "/css/twstyles.css" :media "print" :rel "stylesheet"}]

         [:div {:tw "print:hidden p-4 flex gap-2"}
          [:a {:tw "bg-#23a050 rounded text-white px-2 py-1"
               :href (pages/path-for [:page/placard {:id id}])}
           "Back"]
          [:button {:tw "bg-#23a050 rounded text-white px-2 py-1"
                    :on-click (fn [_] (js/window.print))}
           "Print"]]

         [ui.p/placard-view {:show-qr? true} placard]]))))

(pages/register-page!
 {:page/id :page/print
  :page/view #'page-view
  :page/path "/placard/:id/print"
  :page/parameters {:id :uuid}})
