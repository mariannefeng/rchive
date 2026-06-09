(ns rchive.client.pages.placard
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.auth :as auth]
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
       (if (auth/authed?)
         [:a {:href (pages/path-for [:page/editor {:id id}])}
          "Edit"]
         [:a {:href auth/auth-path}
          "Log In to Edit"])]])))

(pages/register-page!
 {:page/id :page/placard
  :page/view #'page-view
  :page/path "/placard/:id"
  :page/parameters {:id :uuid}})
