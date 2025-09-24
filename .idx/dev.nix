{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.maven
    pkgs.coreutils
  ];

  env = {
    # Global, simple env; no JVM flags here.
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    SPRING_PROFILES_ACTIVE = "dev";
  };

  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "vmware.vscode-boot-dev-pack"
      "google.gemini-cli-vscode-ide-companion"
    ];

    # Tell the Java & Gradle extensions to use JDK 17 and a project-local Gradle home.
    settings = {
      # Java LS / JDT
      "java.jdt.ls.java.home" = "${env:JAVA_HOME}";
      "java.configuration.runtimes" = [
        { name = "JavaSE-17"; path = "${env:JAVA_HOME}"; default = true; }
      ];
      # Gradle (Java extension & Gradle extension)
      "gradle.java.home" = "${env:JAVA_HOME}";
      "java.import.gradle.java.home" = "${env:JAVA_HOME}";
      "gradle.user.home" = "${workspaceFolder}/.gradle-user";
      # Optional: quiet the Gradle server logs
      "gradle.logging.level" = "lifecycle";
    };

    workspace = {
      onCreate = {
        prepare = ''
          set -e
          chmod +x ./gradlew 2>/dev/null || true
          chmod +x ./mvnw  2>/dev/null || true
          mkdir -p .gradle-user .gradle-tmp
          # Don't build here—just make sure wrapper responds.
          (GRADLE_USER_HOME="$PWD/.gradle-user" ./gradlew --version || true)
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
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              mkdir -p .gradle-user .gradle-tmp
              chmod +x ./gradlew 2>/dev/null || true
              export GRADLE_USER_HOME="$PWD/.gradle-user"
              # Keep any global flags from interfering
              export GRADLE_OPTS="" MAVEN_OPTS="" _JAVA_OPTIONS="" JAVA_TOOL_OPTIONS=""
              # Point toolchain discovery straight at JDK 17
              exec ./gradlew \
                --no-daemon \
                --no-configuration-cache \
                -Djava.io.tmpdir="$PWD/.gradle-tmp" \
                -Dorg.gradle.java.installations.paths="$JAVA_HOME" \
                bootRun
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };

        # Optional Maven fallback (also clean)
        spring-maven = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              chmod +x ./mvnw 2>/dev/null || true
              export MAVEN_OPTS="" _JAVA_OPTIONS="" JAVA_TOOL_OPTIONS=""
              exec ./mvnw -DskipTests spring-boot:run
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };
      };
    };
  };
}
