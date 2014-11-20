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
           (- (first %) (* core-potential (Math/exp (* (- beta) (distance-fn core-point (last %))))))
           (last %))
         potentials)))

(defn reject-core
  [potentials
   rejected-point]
  (map #(if (= rejected-point %)
          (list 0 (last rejected-point))
          %)
       potentials))

(defn clusterize
  [points
   distance-fn]
  (let [radius-a 1.5
        radius-b (* radius-a 1.5)
        alpha (/ 4 (Math/pow radius-a 2))
        beta (/ 4 (Math/pow radius-b 2))
        upper-threshold 0.5
        lower-threshold 0.15
        potentials (get-potentials points distance-fn alpha)
        first-core (apply max-key first potentials)
        first-core-potential (first first-core)]
    (println potentials)
    (println "!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    (loop [potentials (update-potentials potentials first-core beta distance-fn)
           cores (list (last first-core))]
      (let [new-core (apply max-key first potentials)
            new-core-potential (first new-core)
            new-core-point (last new-core)]
        (println potentials)
        (println new-core)
        (println "-----------------------------")
        (cond
         (> new-core-potential (* upper-threshold first-core-potential))
           ; accept new cluster center
           (recur (update-potentials potentials new-core beta distance-fn) (conj cores new-core-point))
         (< new-core-potential (* lower-threshold first-core-potential))
           ; end clustering process
           cores
         :else
           (let [dmin (apply min (map #(distance-fn new-core-point %) cores))]
             (if (>= (+ (/ dmin radius-a) (/ new-core-potential first-core-potential)))
               ; accept new cluster center
               (recur (update-potentials potentials new-core beta distance-fn) (conj cores new-core-point))
               ; reject new-core and set it potential to 0.5
               (recur (reject-core potentials new-core) cores))))))))

(defn -main
  [& args]
  ;(if (>= (count args) 2)
    ;(let [data-points (read-file (first args))
     ;     distance (if (= (last args) "hamming") hamming-distance euclid-distance)]
      ;(clusterize data-points distance))
      ;(clusterize '((0 3) (1 5) (2 4)) euclid-distance))
      (clusterize (read-file "resources/butterfly.txt") euclid-distance))
    ;(println "fail")))
