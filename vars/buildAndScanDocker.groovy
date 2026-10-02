def call(Map config = [:]) {
    def registry   = config.get('registry', 'docker.io')
    def imageName  = config.get('imageName', 'shujaahmed198/ratestack-app')
    def credsId    = config.get('dockerHubCredentials', 'dockerhub-creds')
    def tag        = env.BUILD_NUMBER

    stage('Docker Build') {
        sh "docker build -t ${imageName}:${tag} -t ${imageName}:latest ."
    }

    stage('Trivy Image Scan') {
        sh "trivy image --severity HIGH,CRITICAL ${imageName}:${tag} || true"
    }

    stage('Docker Push') {
        withCredentials([usernamePassword(credentialsId: credsId, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh """
                echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                docker push ${imageName}:${tag}
                docker push ${imageName}:latest
            """
        }
    }
}
