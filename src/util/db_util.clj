(ns util.db-util
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]
            ))

(s/def ::order vector?)
(s/def ::schema map?)
(s/def ::schema-map (s/keys :req-un [::order ::schema]))

(s/fdef ddl-seq
  :args (s/cat :edn string?)
  :ret vector?)
  ;:ret ::schema-map)

(defn ddl-seq
  "Read jdbc/create-table-ddl style schema from edn file.
   Return sequence of create-table-ddl"
  [edn]
  (edn/read-string edn)
  [1 2 3])

(st/instrument) ;; TODO - NOTE
(ddl-seq (slurp "./DB/sqlite/szmc_schema_0.1.0.edn"))
;(ddl-seq 1)
