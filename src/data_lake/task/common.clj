(ns data-lake.task.common
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [java-time :refer [offset-date-time]]
            [data-lake.consts :refer :all]
            ))


;(defmulti run-task (fn [& args] (when args (first args))))
