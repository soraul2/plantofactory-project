# 1. Java 21 JDK를 기본으로 시작
FROM openjdk:21-jdk-slim

# 2. Node.js 20 설치 (NVM 사용)
# 'node'라는 이름의 non-root 유저를 만들고, NVM(Node Version Manager)을 설치
ARG USERNAME=node
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && apt-get update \
    && apt-get install -y curl git sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# 'node' 유저로 전환
USER $USERNAME

# NVM 설치
ENV NVM_DIR /home/$USERNAME/.nvm
ENV NODE_VERSION 20
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash \
    && . $NVM_DIR/nvm.sh \
    && nvm install $NODE_VERSION \
    && nvm use $NODE_VERSION \
    && nvm alias default $NODE_VERSION

# 터미널 시작 시 NVM 자동 로드
ENV PATH $NVM_DIR/versions/node/v$NODE_VERSION/bin:$PATH
RUN echo "\n# Load NVM" >> /home/$USERNAME/.bashrc && \
    echo "export NVM_DIR=\"\$HOME/.nvm\"" >> /home/$USERNAME/.bashrc && \
    echo "[ -s \"\$NVM_DIR/nvm.sh\" ] && \. \"\$NVM_DIR/nvm.sh\"" >> /home/$USERNAME/.bashrc && \
    echo "[ -s \"\$NVM_DIR/bash_completion\" ] && \. \"\$NVM_DIR/bash_completion\"" >> /home/$USERNAME/.bashrc

# (선택) Gradle 설치 (Spring Boot 래퍼(gradlew)를 사용한다면 필수는 아님)