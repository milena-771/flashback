pipeline {
    agent {
        label 'jenkins-agent'
    }
    environment {
        BUILD_API_JOB = 'flashback-business-build-pipeline'
        RUN_API_JOB = 'flashback-business-run-pipeline'
         BUILD_UI_JOB = 'flashback-ui-build-pipeline'
    }

    stages {
        stage('Test de la branche Main') {
            steps {
                echo 'Pipeline lancé pour la branche main !'
                echo 'Vérification du pipeline réussie.'
            }
        }
        stage('Build FB API') {
            steps {
                echo 'Build FB API loading....'
                // Lancer le job Jenkins pour le projet Spring Boot
                build job: BUILD_API_JOB
                echo 'Build FB API Finished !'
            }
        }
        stage('Run FB API') {
            steps {
                echo 'Run FB API loading....'
                build job: RUN_API_JOB
                echo 'Run FB API is running !'
            }
        }
        stage('Build UI') {
            steps {
                echo 'Build UI loading....'
                build job: BUILD_UI_JOB
                echo 'Buil UI Finished !'
            }
        }
    }

    post {
        success {
            echo 'Le pipeline a été exécuté avec succès.'
        }
        failure {
            echo 'Le pipeline a échoué.'
        }
    }
}
