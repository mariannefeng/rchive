(ns rchive.client.pages.placard
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.ui.placard :as placard]
   [rchive.client.remote :as remote]))

(defn page-view
  [[_ {:keys [id]}]]
  (r/with-let
   [*placard (remote/tada-atom [:api/placard {:id id}])]
   (when @*placard
     [:div
      [placard/placard-view {:show-qr? false} @*placard]

      [:div
       [:a {:href (pages/path-for [:page/gallery])}
        "Back to All"]]

      [:div
       [:a {:href (pages/path-for [:page/editor {:id id}])}
        "Edit"]]])))

(pages/register-page!
 {:page/id :page/placard
  :page/view #'page-view
  :page/path "/placard/:id"
  :page/parameters {:id :uuid}})
