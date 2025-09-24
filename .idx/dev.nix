{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.maven
    pkgs.coreutils
  ];

  env = {
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    SPRING_PROFILES_ACTIVE = "dev";
    # Do NOT set JAVA_TOOL_OPTIONS globally.
  };

  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "google.gemini-cli-vscode-ide-companion"
    ];

    workspace = {
      onCreate = {
        install = ''
          set -euo pipefail
          mkdir -p .gradle-user .gradle-tmp
          chmod +x ./gradlew 2>/dev/null || true

          export GRADLE_USER_HOME="$PWD/.gradle-user"
          export _JAVA_OPTIONS=""
          export JAVA_TOOL_OPTIONS=""
          export MAVEN_OPTS=""
          export GRADLE_OPTS=""

          if [ -x ./gradlew ]; then
            ./gradlew \
              --no-daemon \
              --no-configuration-cache \
              -Djava.io.tmpdir="$PWD/.gradle-tmp" \
              build
          else
            bash ./gradlew \
              --no-daemon \
              --no-configuration-cache \
              -Djava.io.tmpdir="$PWD/.gradle-tmp" \
              build
          fi
        '';
      };
      onStart = {
        verify-java = "java -version";
        default.openFiles = [ "src/main/AiChatServiceApplication.java" ];
      };
    };

    previews = {
      enable = true;

      previews = {
        spring-gradle = {
          manager = "gradle";
          cwd = "";
          # wrap in a login shell to use $PWD reliably
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              mkdir -p .gradle-user .gradle-tmp
              chmod +x ./gradlew 2>/dev/null || true
              export GRADLE_USER_HOME="$PWD/.gradle-user"
              export _JAVA_OPTIONS=""
              export JAVA_TOOL_OPTIONS=""
              export MAVEN_OPTS=""
              export GRADLE_OPTS=""
              exec ./gradlew \
                --no-daemon \
                --no-configuration-cache \
                -Djava.io.tmpdir="$PWD/.gradle-tmp" \
                bootRun
            ''
          ];
          env = {
            SPRING_PROFILES_ACTIVE = "dev";
          };
        };

        # Optional Maven preview (isolated too)
        spring-maven = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              mkdir -p .maven-user .maven-tmp
              chmod +x ./mvnw 2>/dev/null || true
              export MAVEN_OPTS=""
              export _JAVA_OPTIONS=""
              export JAVA_TOOL_OPTIONS=""
              export MAVEN_USER_HOME="$PWD/.maven-user"
              exec ./mvnw -DskipTests spring-boot:run
            ''
          ];
          env = {
            SPRING_PROFILES_ACTIVE = "dev";
          };
        };
      };
    };
  };
}
