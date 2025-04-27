pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Test de la branche Main') {
            steps {
                echo 'Pipeline lancé pour la branche main !'
                echo 'Vérification du pipeline réussie.'
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
