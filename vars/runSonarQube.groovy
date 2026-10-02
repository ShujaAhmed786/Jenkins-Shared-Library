def call(Map config = [:]) {
    def serverName = config.get('serverName', 'SonarQube')
    def projectKey = config.get('projectKey', 'ratestack')
    def sources    = config.get('sources', 'src')

    stage('SonarQube SAST Analysis') {
        withSonarQubeEnv(serverName) {
            sh """
                sonar-scanner \
                  -Dsonar.projectKey=${projectKey} \
                  -Dsonar.sources=${sources} \
                  -Dsonar.exclusions=**/node_modules/**,**/.next/**
            """
        }
    }
}
