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
   coef]
  (list
   (reduce +
           (map #(Math/exp (* (- coef) %))
                (map #(distance-fn p %) points)))
   p))

(defn get-potentials
  [points
   distance-fn
   alpha]
  (map #(get-point-potential % points distance-fn alpha) points))

(defn update-potentials
  [potentials
   core
   beta
   distance-fn]
  (let [core-potential (first core)
        core-point (last core)]
    (map #(list
           (- (first %) (* core-potential (Math/exp (distance-fn core-point (last %)))))
           (last %))
         potentials)))

(defn clusterize
  [points
   distance-fn]
  (let [radius-a 1.5
        radius-b (* radius-a 1.5)
        alpha (/ 4 (Math/pow radius-a 2))
        beta (/ 4 (Math/pow radius-b 2))
        potentials (get-potentials points distance-fn alpha)
        first-core (apply max-key first potentials)]
    (println potentials)
    (update-potentials potentials first-core beta distance-fn)))

(defn -main
  [& args]
  ;(if (>= (count args) 2)
    ;(let [data-points (read-file (first args))
     ;     distance (if (= (last args) "hamming") hamming-distance euclid-distance)]
      ;(clusterize data-points distance))
      (clusterize '((0 3) (1 5) (2 4)) euclid-distance))
    ;(println "fail")))
