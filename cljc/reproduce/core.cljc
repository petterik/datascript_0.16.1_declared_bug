(ns reproduce.core
  (:require [datascript.db :as db]
            [datascript.core :as d])
  (:gen-class))

(defn datascript-test-case []
  (let [datom (db/->Datom 1 :foo "bar" 0 true)]
    (nth datom 0)))



(defn ^:declared fn-declared [i])

(defn minimal-test-case []
  (fn-declared 1))

(defn fn-declared [^long i]
  "ok")



(defn ^:declared declared-type-hinted [^long i])

(defn type-hinted-case []
  (declared-type-hinted 1))

(defn declared-type-hinted [^long i]
  "ok")

 

(set! *warn-on-reflection* true)

(defn ^:declared declared-primitive-types-hinted [^long i obj])

(defn primitive-types-hinted-case []
  (declared-primitive-types-hinted 1 ""))

(defn declared-primitive-types-hinted [^long i ^String obj]
  (.length obj))


(defn get-unique-by-value-test-case []
  (let [conn (d/create-conn {:uni {:db/unique :db.unique/identity}})]
    (d/transact conn [{:uni :foo}])
    (assert (some? (:db/id (d/entity (d/db conn) [:uni :foo]))))))

(defn -main [& args]
  (try
    (prn "datascript")
    (datascript-test-case)
    (prn "it went ok")
    (catch Throwable ex
      (prn ex)))
  (try
    (prn "minimal")
    (minimal-test-case)
    (prn "it went ok")
    (catch Throwable ex
      (prn ex)))
  (try
    (prn "type hinted")
    (type-hinted-case)
    (prn "it went ok")
    (catch Throwable ex
      (prn ex)))
  (try
    (prn "primitive types hinted")
    (primitive-types-hinted-case)
    (prn "it went ok")
    (catch Throwable ex
      (prn ex)))
  (try
    (prn "test getting unique by value")
    (get-unique-by-value-test-case)
    (prn "it went ok")
    (catch Throwable ex
      (prn ex))))

