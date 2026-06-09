(ns rchive.client.pages.gallery
  (:require
   [bloom.commons.pages :as pages]
   [reagent.core :as r]
   [rchive.client.auth :as auth]
   [rchive.client.remote :as remote]))

(defn page-view
  []
  (r/with-let
   [*placards (remote/tada-atom [:api/placards])]
   [:div {:tw "flex flex-col justify-center items-center"}
    (for [placard @*placards]
      ^{:key (:placard/id placard)}
      [:a {:href (pages/path-for [:page/placard {:id (:placard/id placard)}])}
       (:placard/title placard)])

    (if (auth/authed?)
      [:button
       {:on-click (fn []
                    (-> (remote/tada! [:api/create-placard!])
                        (.then (fn [{:keys [id]}]
                                 (pages/navigate-to! [:page/editor {:id id}])))))}
       "Add a Placard"]
      [:button {:on-click (fn [_]
                            (auth/auth!))}
       "Log In to Add a Placard"])]))

(pages/register-page!
 {:page/id :page/gallery
  :page/view #'page-view
  :page/path "/"})
