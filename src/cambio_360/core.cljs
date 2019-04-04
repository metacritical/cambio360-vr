(ns cambio-360.core
  (:require [cambio-360.playlist :as playlist]
            [cambio-360.scene :as scene]))

(enable-console-print!)

(defn main []
  (.log js/console "Cambio360 Video/VR Demo!")
  (scene/init))   

(set! (.-onload js/window) main)
