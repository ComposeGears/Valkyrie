package io.github.composegears.valkyrie.psi

object DeepNestedIconPack {
    // Deep linear chain
    object Level1 {
        object Level2 {
            object Level3 {
                object Level4 {
                    object Level5
                }
            }
        }
    }

    // Multiple branches at different levels
    object Branch {
        object Left {
            object LeftDeep1 {
                object LeftDeep2
            }
        }

        object Middle

        object Right {
            object RightDeep1
            object RightDeep2
        }
    }

    // Wide tree
    object Wide {
        object Item1
        object Item2
        object Item3
        object Item4
        object Item5
    }

    // Single leaf
    object Single
}

