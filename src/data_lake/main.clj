(ns data-lake.main
  (:require [util.db-util :as dbu]
            [clojure.tools.cli :as cli]
            [data-lake.task.sqlite :as sqlite]
            )
  (:gen-class))

(def cli-options
  [["-h" "--help"]])

(def help-msg "help-msg\n")
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [n-args (count args)]
    (cond (= n-args 0) (print help-msg)
  )))
  ;(println args "Hello, World!")
  ;(clojure.pprint/pprint (cli/parse-opts args cli-options))
  ;(let [cmd-map (cli/parse-opts args cli-options)]

(apply -main ["sqlite" "new" "szmc.db" "./DB/sqlite/szmc_schema_0.1.0.edn"])
(apply -main [])

