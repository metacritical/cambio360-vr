(ns cambio-360.controls)

(def URL (or js/window.URL js/window.webkitURL))

(defn q-select [q] (.querySelector js/document q))

(defn vid-input [] (q-select "#vid-input"))

(defn set-video-file [video file]
  (doto video
    (set! -id "vr-player")
    (set! -crossOrigin "true")
    (set! -src file)
    (.load)
    (.setAttribute "webkit-playsinline" "true")
    (.setAttribute "playsinline" "true")))

(defn select-vid-file [video ev]
    (let [file (aget (.-files (vid-input)) 0)
          file-url (.createObjectURL URL file)]
    (set-video-file video file-url)))



