(ns trickle.main
  (:import [trickle TrickleRootNode TrickleNode TrickleFnCallNode TrickleNumberNode TrickleLanguage]
           [com.oracle.truffle.api Truffle]
           [org.graalvm.polyglot Context Source])
  (:require [clojure.edn :as edn])
  (:gen-class))

(set! *warn-on-reflection* true)

#_(def +-node (proxy [TrickleNode] []
              (execute [virtual-frame]
                +)))

(defn ->node [edn]
  (cond (number? edn) (TrickleNumberNode. edn (str edn))
        (list? edn) (TrickleFnCallNode.
                     (str (first edn))
                     (into-array TrickleNode (map ->node (rest edn))))))

(defn -main [& [expr]]
  (let [rn (TrickleRootNode.
            (->node (edn/read-string expr)))
        _ (set! TrickleLanguage/rootNode rn)
        ;; ctx (.build (Context/newBuilder (into-array String ["trickle"])))
        ;; src (.build (Source/newBuilder "trickle" (java.io.File. "src/trickle/main.clj")))
        ;; res (.eval ctx src)
        ct (.createCallTarget (Truffle/getRuntime) rn)
        res (.call ct (into-array []))]
    (prn res)))
