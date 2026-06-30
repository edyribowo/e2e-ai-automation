pipeline {
    agent any

    environment {
        JAVA_TOOL_OPTIONS = '-Dfile.encoding=UTF-8'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                withCredentials([string(credentialsId: 'REQRES_API_KEY', variable: 'REQRES_API_KEY')]) {
                    sh 'mvn clean test -DREQRES_API_KEY=${REQRES_API_KEY}'
                }
            }
        }

        stage('Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk             : '',
                    results         : [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
        }
    }
}
