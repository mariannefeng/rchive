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
       [:div {:tw "m-2"}
        [:a {:tw "bg-#23a050 rounded text-white px-2 py-1 mr-2"
             :href (pages/path-for [:page/gallery])}
         "Back to All"]


        (if (auth/authed?)
          [:a {:tw "bg-#23a050 rounded text-white px-2 py-1"
               :href (pages/path-for [:page/editor {:id id}])}
           "Edit"]
          [:a {:tw "bg-#23a050 rounded text-white px-2 py-1"
               :href auth/auth-path}
           "Log In to Edit"])]

       [:div {:tw "ml-2 mt-10 text-center flex flex-col gap-4"}
        [placard/artists @*placard]
        [placard/title-and-year @*placard]
        [placard/materials @*placard]
        [placard/description @*placard]]])))


 

(pages/register-page!
 {:page/id :page/placard
  :page/view #'page-view
  :page/path "/placard/:id"
  :page/parameters {:id :uuid}})
