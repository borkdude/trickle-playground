(ns trickle.main
  (:import [trickle TrickleRootNode TrickleNode TrickleFnCallNode TrickleIntNode]
           [com.oracle.truffle.api Truffle])
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
        ct (.createCallTarget (Truffle/getRuntime) rn)
        res (.call ct (into-array []))]
    (prn res)))
