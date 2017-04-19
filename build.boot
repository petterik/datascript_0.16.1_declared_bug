(set-env!
 :source-paths #{"cljc"}
 :dependencies '[[org.clojure/clojure         "1.9.0-alpha14"]
                 [datascript "0.16.1"]])

(require '[boot.core :as core]
         '[boot.pod :as pod])

(defn aot'
  "Perform AOT compilation of Clojure namespaces."
  [& {:keys [all namespace compiler-options]}]
  (let [tgt         (core/tmp-dir!)
        pod-env     (update-in (core/get-env) [:directories] conj (.getPath tgt))
        compile-pod (future (pod/make-pod pod-env))]
    (core/with-pre-wrap [fs]
      (core/empty-dir! tgt)
      (let [all-nses (->> fs core/fileset-namespaces)
            nses     (->> all-nses (clojure.set/intersection (if all all-nses namespace)) sort)]
        (pod/with-eval-in @compile-pod
          (binding [*compile-path* ~(.getPath tgt)
                    clojure.core/*compiler-options* '~compiler-options]
            (doseq [[idx ns] (map-indexed vector '~nses)]
              (boot.util/info "Compiling %s/%s %s...\n" (inc idx) (count '~nses) ns)
              (compile ns)))))
      (-> fs (core/add-resource tgt) core/commit!))))

(deftask make []
  (comp
   (aot' :all true
        :compiler-options {:direct-linking true})
   (pom :project 'reproduce
        :version "1.0.0")
   (uber)
   (jar :file "reproduce.jar"
        :main 'reproduce.core)
   (sift :include #{#"reproduce.jar"})
   (target :dir #{"out"})))
