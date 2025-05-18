pipeline {
    agent {
        label 'jenkins-agent'
    }
    environment {
        BUILD_API_JOB = 'flashback-buisness-build-pipeline'
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
