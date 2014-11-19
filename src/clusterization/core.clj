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
  [p1 p2]
  (reduce + (map #(if (= %1 %2) 0 1) p1 p2)))

(defn euclid-distance
  [p1 p2]
  (Math/sqrt (reduce + (map #(Math/pow % 2) (map - p1 p2)))))

(defn get-point-potential
  [p
   points
   distance-fn
   alpha]
  (reduce + (map #(Math/exp (* (- alpha) %)) (map #(distance-fn p %) points))))

(defn get-potentials
  [points
   distance-fn]
  (println points))

(defn -main
  [& args]
  ;(if (>= (count args) 2)
    ;(let [data-points (read-file (first args))
     ;     distance (if (= (last args) "hamming") hamming-distance euclid-distance)]
      ;(clusterize data-points distance))
      (get-potentials '((0 3) (1 5) (2 4)) euclid-distance))
    ;(println "fail")))
