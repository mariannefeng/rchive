(ns rchive.git
  (:require
   [clj-jgit.porcelain :as git]))

(defn repo
  [data-dir]
  (git/load-repo data-dir))

(def memo-repo
  (memoize repo))

(defn add-commit-and-push!
  [{:keys [committer-name
           committer-email
           author-name
           author-email
           repo-dir
           ssh-key-dir
           ssh-key-name
           message]}]
  (git/git-add (memo-repo repo-dir) ".")
  (git/git-commit (memo-repo repo-dir)
                  message
                  :author
                  {:name author-name
                   :email author-email}
                  :committer
                  {:name committer-name
                   :email committer-email})
  (git/with-identity {:name ssh-key-name
                      :key-dir ssh-key-dir
                      :trust-all? true}
    (git/git-push (memo-repo repo-dir))))



#_(add-commit-and-push! {:committer-name "rchivebot"
                         :committer-email "rchive@recurse.com"
                         :author-email "feng.marianne@gmail.com"
                         :author-name "mariannefeng"
                         :repo-dir "data"
                         :ssh-key-dir "ssh"
                         :ssh-key-name "github"
                         :message "im a commit"})