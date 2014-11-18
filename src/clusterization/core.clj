(ns clusterization.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (import java.lang.Math))

(defn parse-str
  [str]
  (map #(Integer/parseInt %)
       (drop-last (string/split str #","))))

(defn read-file
  [file-name]
  (with-open [rdr (io/reader file-name)]
    (doall (map parse-str (line-seq rdr)))))

(defn hamming-distance
  [x1 x2]
  (println "hamming"))

(defn euclid-distance
  [x1 x2]
  (println "euclid"))

(defn count-potentials
  [data-point alpha]
  (println "count potentials"))

(defn clusterize
  [data-points distance]
  (let [radius-a 1.5
        radius-b (* radius-a 1.5)
        alpha (/ 4 (* radius-a radius-a))
        beta (/ 4 (* radius-b radius-b))
        upper-threshold 0.5
        lower-threshold 0.15
        potentials (count-potentials data-points alpha)]
    (println "clusterize")))

(defn -main
  [& args]
  (if (>= (count args) 2)
    (let [data-points (read-file (first args))
          distance (if (= (last args) "hamming") hamming-distance euclid-distance)]
      (clusterize data-points distance))
    (println "fail")))
