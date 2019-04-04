(ns cambio-360.scene
  (:require [cljsjs.three]
            [cljsjs.three-examples.controls.OrbitControls]
            [cambio-360.ui :as ui]))

;;Other 3js effects and controls
;; [cljsjs.three-examples.effects.StereoEffect]
;; [cljsjs.three-examples.controls.PointerLockControls]
;; [cljsjs.three-examples.controls.DeviceOrientationControls]

(def renderer (js/THREE.WebGLRenderer.))
(def render-type (atom nil))


(defn width [] js/window.innerWidth)
(defn height [] js/window.innerHeight)

(def scene (js/THREE.Scene.))
(def focus (js/THREE.Vector3. -200 250 0))
(def camera (js/THREE.PerspectiveCamera. 
             70 (/ (width) (height)) 1 1000))

(set! (.-position.x camera) (.-x focus))
(set! (.-position.y camera) (.-y focus))
(set! (.-position.z camera) (.-z focus))
(.lookAt camera focus)

;; (def effect (js/THREE.StereoEffect. renderer))
;; (defn set-effect-size []
  ;; (.setSize effect js/window.innerWidth js/window.innerHeight))

(def video-el (js/document.createElement "video"))

(defn get-elem [id] (js/document.getElementById id))

;;Grid Helpers
(def grid-helper (js/THREE.GridHelper. 1000, 30, "red", "grey"))
(.add scene grid-helper)
(set! (.-position.y grid-helper) -30)
(set! (.-position.x grid-helper) -30)
(set! (.-position.z grid-helper) 20)

;;Respond to resize
(defn resize []
  (.setSize renderer (/ (width) 1.2) (/ (height) 1.2))
  (set! (.-aspect camera) (/ (width) (height)))
  (.updateProjectionMatrix camera))

(defn setsize []
  (.addEventListener js/window "resize" resize false))

(defn append-renderer []
  (resize)
  (.appendChild (get-elem "player") (.-domElement renderer)))

(defn set-renderer [type]
  (reset! render-type type))

(def controls (js/THREE.OrbitControls. camera))
(set! (.-enableZoom controls) false)

;; (def controls (js/THREE.DeviceOrientationControls. camera))

(defn set-camera-position []
  (aset camera "position" "z" 200))

(def geometry (js/THREE.SphereBufferGeometry. 500 60 40))
(defn set-geometric-scale []
  (.scale geometry -1 1 1))

(def texture (js/THREE.VideoTexture. video-el))
(defn set-texture-filters []
  (doto texture
    (set! -minFilter js/THREE.LinearFilter)
    (set! -format js/THREE.RGBFormat)))

(def material (js/THREE.MeshBasicMaterial. #js{:map texture}))
(def mesh (js/THREE.Mesh. geometry material))
;; (set! (.-material.wireframe mesh) true)

(defn set-mesh-rotation []
  (set! mesh.rotation.y (- (/ js/Math.PI 2))))

(defn animate []
  (.requestAnimationFrame js/window animate)
  (.update controls)
  (.render @render-type scene camera))

(defn load-scene []
  (append-renderer)
  (set-renderer renderer)
  (set-camera-position)
  (set-geometric-scale)
  (set-texture-filters)
  (set-mesh-rotation)
  (setsize)
  (.add scene mesh)
  (animate))

(defn init []
  (ui/render video-el load-scene))
