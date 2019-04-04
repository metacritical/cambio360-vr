(ns cambio-360.ui
  (:require [reagent.core :as r]
            [cambio-360.controls :as controls]))

(defn player [video]
  [:<>
   [:div#player]
   [:div#player-controls
    [:div.menu
     [:div.title
      [:i.fas.fa-play {:on-click #(.play video)}]]]]])

(defn sidebar [video]
  [:div#sidebar
   [:input#vid-input {:type :file :accept "video/*"
                      :on-change #(controls/select-vid-file video false)}]])

(defn menubar []
  [:<>
  [:div#menubar
   [:div.menu
    [:div#play.title "File"]]
   [:div.menu
    [:div.title "Add"]
    [:div.options
     [:div.option "Shapes"]
     [:hr.HorizontalRule]
     [:div.option "Plane"]
     [:div.option "Box"]
     [:div.option "Circle"]
     [:div.option "Nurb-Surface"]]]]])

(defn toolbar []
  [:<>
  [:div#menubar
   [:div.menu
    [:div#play.title "ICON1"]]
   [:div.menu
    [:div.title "ICON2"]]]])


(defn app-container [el]
  [:div
   [menubar]
   [player el]
   [sidebar el]])

(defn render [el load-scene]
  (r/render-component
   [(r/create-class
     {:display-name "app-container"
      :component-did-mount load-scene
      :reagent-render
      (fn []
        [app-container el])})]
   (.getElementById js/document "app")))
