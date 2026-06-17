(ns rchive.client.pages.gallery
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.auth :as auth]
   [rchive.client.remote :as remote]
   [rchive.client.ui.placard :as placard]))

(defn page-view
  []
  (r/with-let
    [*placards (remote/tada-atom [:api/placards])]
    [:div
     (if (auth/authed?)
       [:button 
        {:tw "bg-#23a050 rounded text-white px-1 mt-2 ml-2"
        :on-click (fn []
                     (-> (remote/tada! [:api/create-placard!])
                         (.then (fn [{:keys [id]}]
                                  (pages/navigate-to! [:page/editor {:id id}])))))}
        "+ Add a Placard"]
       [:button {:tw "bg-#23a050 rounded text-white px-1 mt-2 ml-2"
                 :on-click (fn [_]
                             (auth/auth!))}
        "Log In to Add a Placard"])
     [:div {:tw "flex flex-wrap"}
      (for [placard @*placards]
        ^{:key (:placard/id placard)}
        [:a
         {:href (pages/path-for [:page/placard {:id (:placard/id placard)}])}
         [placard/placard-simple placard]])]]))

(pages/register-page!
 {:page/id :page/gallery
  :page/view #'page-view
  :page/path "/"})
