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
  (if (= (count args) 0)
      (print help-msg)
      (let [task (first args)]
        (cond 
          (= task "help") 
          (print help-msg)

          (= task "sqlite") 
          (apply sqlite/run-cmd (rest args))
      ))))
  ;(println args "Hello, World!")
  ;(clojure.pprint/pprint (cli/parse-opts args cli-options))
  ;(let [cmd-map (cli/parse-opts args cli-options)]
