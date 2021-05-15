(ns trickle.main
  (:import [trickle TrickleRootNode TrickleNode TrickleFnCallNode TrickleIntNode TrickleLanguage]
           [com.oracle.truffle.api Truffle]
           [org.graalvm.polyglot Context Source])
  (:gen-class))

(set! *warn-on-reflection* true)

(def +-node (proxy [TrickleNode] []
              (execute [virtual-frame]
                +)))

(defn -main [& _]
  (let [rn (TrickleRootNode.
            (TrickleFnCallNode.
             +-node
             (into-array TrickleNode
                         [(TrickleIntNode. 1)
                          (TrickleIntNode. 2)])))
        _ (set! TrickleLanguage/rootNode rn)
        ctx (.build (Context/newBuilder (into-array String ["trickle"])))
        src (.build (Source/newBuilder "trickle" (java.io.File. "src/trickle/main.clj")))
        res (.eval ctx src)
        #_#_ct (.createCallTarget (Truffle/getRuntime) rn)
        #_#_res (.call ct (into-array []))]
    (prn (str res))))
