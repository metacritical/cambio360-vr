(ns cambio-360.core
    (:require [cljsjs.three]
              [cljsjs.three-examples.controls.OrbitControls]
              [cljsjs.three-examples.controls.DeviceOrientationControls]
              [cljsjs.three-examples.controls.PointerLockControls]
              [cljsjs.three-examples.effects.StereoEffect]))

(enable-console-print!)

(defonce app-state (atom {:render-type nil}))

(declare resize)

(def scene (js/THREE.Scene.))
(def camera (js/THREE.PerspectiveCamera. 
             75 (/ (.-innerWidth js/window) (.-innerHeight js/window)) 1 1000))
(def renderer (js/THREE.WebGLRenderer.))

(def effect (js/THREE.StereoEffect. renderer))
(defn set-effect-size []
  (.setSize effect js/window.innerWidth js/window.innerHeight))

(def video (js/document.createElement "video"))

(defn set-video []
  (doto video
    (set! -width js/window.innerWidth)
    (set! -height js/window.innerHeight)
    (set! -crossOrigin "anonymous")
    (set! -src "/video/London-360.mp4"))
  (.load video)
  (.setAttribute video "webkit-playsinline" "true")
  (.setAttribute video "playsinline" "true"))

(defn set-renderer []
  (resize)
  (.appendChild js/document.body (.-domElement renderer)))

(defn set-render-type [type]
  (reset! app-state {:render-type type}))

;; (def controls (js/THREE.OrbitControls. camera))
;; (set! (.-enableZoom controls) false)

(def controls (js/THREE.DeviceOrientationControls. camera))

;; (def controls (js/THREE.PointerLockControls. camera))

(defn set-camera-position []
  (aset camera "position" "z" 200))

(def geometry (js/THREE.SphereBufferGeometry. 500 60 40))
(defn set-geometric-scale []
  (.scale geometry -1 1 1))

(def texture (js/THREE.VideoTexture. video))
(defn set-texture-filters []
  (doto texture
    (set! -minFilter js/THREE.LinearFilter)
    (set! -format js/THREE.RGBFormat)))

(def material (js/THREE.MeshBasicMaterial. #js{:map texture}))
(def mesh (js/THREE.Mesh. geometry material))
(defn set-mesh-rotation []
  (set! mesh.rotation.y (- (/ js/Math.PI 2))))

(defn animate []
  (.requestAnimationFrame js/window animate)
  (.update controls)
  (.render (@app-state :render-type) scene camera))

(defn get-elem [id] (js/document.getElementById id))

(defn setup-buttons []
  (let [vr-btn (get-elem "vr-btn")]
    (vr-btn.addEventListener "click" (fn [] (set-render-type effect)
                                       (.play video)) false)))

;;Respond to resize
(defn resize []
  (.setSize renderer js/window.innerWidth js/window.innerHeight)
  (set! (.-aspect camera) (/ js/window.innerWidth js/window.innerHeight))
  (.updateProjectionMatrix camera))

(defn setsize []
  (.addEventListener js/window "resize" resize false))

(defn main []
  (.log js/console "360 Video/VR Demo!")
  (set-video)
  (set-renderer)
  (set-render-type renderer)
  (set-effect-size)
  (set-camera-position)
  (set-geometric-scale)
  (set-texture-filters)
  (set-mesh-rotation)
  (setup-buttons)
  (setsize)
  (.add scene mesh)
  (animate))

(set! (.-onload js/window) main)
